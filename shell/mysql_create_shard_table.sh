#!/usr/bin/env bash

declare -i start_index
declare -i end_index
host=""
my_user=""
my_password=""
database_action="create"
database_name_prefix=""
sql_file=""

create_database="create database if not exists"
drop_database="drop database if exists"

#-a 表示一个选项， 参数可选
#选项后面的冒号表示该选项需要参数
while getopts "h:u:p:dn:s:b:e:" arg
do
    case $arg in
        h)
            echo "url:port: $OPTARG"
            host=$OPTARG
        ;;
        u)
            echo "user: $OPTARG"
            my_user=$OPTARG
        ;;
        p)
            echo "password: $OPTARG"
            my_password=$OPTARG
        ;;
        d)
            echo "drop database"
            database_action="drop"
        ;;
        n)
            echo "database name prefix: $OPTARG"
            database_name_prefix=$OPTARG
        ;;
        s)
            echo "sql file: $OPTARG"
            sql_file=$OPTARG
        ;;
        b)
            echo "begin index: $OPTARG"
            start_index=$OPTARG
        ;;
        e)
            echo "end index: $OPTARG"
            end_index=$OPTARG
        ;;
        ?) #当有不认识的选项的时候arg为?
            echo "unkonw argument"
            exit 1
        ;;
    esac
done

#传入参数
if [[ -z ${host} ]];
then
    echo "non host"
    exit 1
fi
if [[ -z ${my_user} ]];
then
    echo "non user"
    exit 1
fi
if [[ -z ${my_password} ]];
then
    echo "non password"
    exit 1
fi
if [[ -z ${database_name_prefix} ]];
then
    echo "non database_name_prefix"
    exit 1
fi
if [[ -z ${start_index} ]];
then
    echo "non start_index"
    exit 1
fi
if [[ -z ${end_index} ]];
then
    echo "non end_index"
    exit 1
fi
#文件参数存在， 文件存在
if [[ -z ${sql_file} ]] || [[ ! -e $sql_file ]]; then
    echo "non sql file"
    exit
fi

MYCMD="mysql -u$my_user -p$my_password"
echo `mysql cmd: `$MYCMD
for (( index = start_index; index < end_index; ++ index ))
do
    dbname=${database_name_prefix}${index}

    echo "$database_action $dbname;"
    if [[ ${database_action} = "create" ]]
    then
        echo -e "${create_database} $dbname;"
        $MYCMD -e "${create_database} $dbname;"
        $MYCMD -D${dbname} < $sql_file
    else
        echo -e "${drop_database} $dbname;"
        $MYCMD -e "${drop_database} $dbname;"
    fi
done

