# Lab6Server
The rep for server
Technical doc


remove {element}: удалить элемент из коллекции по его значению   //1

show: вывести в стандартный поток вывода все элементы коллекции в строковом представлении	//2

add_if_max {element}: добавить новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции	//3

load: загрузить коллекцию на сервер	//4

info: вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)	//5

import {String path}: добавить в коллекцию все данные из файла	//6

add {element}: добавить новый элемент в коллекцию	//7

start: начать выполнение программмы		//8

exit: выйти из программмы	//9

change_def_file_path {String path}: меняет путь к файлу с коллекцией на новый.	//10

help: справка	//11

login {loginStr} {passwordStr}: залогиннится на сервере.(Пароль должен быть не менее 8 символов) //110

save: сохранить коллекцию с сервера //12

edit {costume_id} {costume_field.field_of_costume_field}: редактирует указанную характеристику объекта коллекции //13


Комманда show была переделан в следующий формат:
show {userName} {} - выводит все костюмы пользователя кратко
show {} {} - выводит все костюмы всех пользователей кратко
show {} {costume_id} - выводит подробную информацию о костюме с id = costume_id

{"topClothes":{"growth_sm":170,"size":50,"color":"White","material":"Chlopoc","is_hood":false,"name":"T-Shirt","is_for_man":true,"hand_sm_length":60},"downClothes":{"size":50,"color":"Black","material":"Chlopoc","diametr_leg_sm":40,"name":"Trousers","leg_length_sm":70,"is_for_man":true},"underwear":{"sex_lvl":99,"size":25,"color":"Red","material":"Chlopoc","name":"Panties","is_for_man":true},"hat":{"cylinder_height_sm":15,"size":50,"color":"White","material":"Len","visor_length_sm":20,"name":"BaseballHat","is_for_man":true},"shoes":{"is_shoelaces":true,"size":38,"color":"White","material":"Leather","outsole_material":"Rubber","name":"Sneackers","is_for_man":true}}

Empty Transfer Package //666

OK //0
ERROR //-1

Aditional Data to import //601

Checking Connection Transfer Package //101

Logging Package //110

Disconnecting //111

Collection updating show command //156

Reconnected //228

Server Shuted Down //9999

json regex:

{"topClothes":{"growth_sm":(\d+),"size":(\d+),"color":"(White|Black|Green|Purple|Blonde|Blue|Red|Orange|Gray|Brown)","material":"(Chlopoc|Leather|Wool|Sintetic|Chlopoc|Len|Rubber)","is_hood":(true|false),"name":"(.+)","is_for_man":(true|false),"hand_sm_length":(\d+)},"downClothes":{"size":(\d+),"color":"(White|Black|Green|Purple|Blonde|Blue|Red|Orange|Gray|Brown)","material":"(Chlopoc|Leather|Wool|Sintetic|Chlopoc|Len|Rubber)","diametr_leg_sm":(\d+),"name":"(.+)","leg_length_sm":(\d+),"is_for_man":(true|false)},"underwear":{"sex_lvl":(\d+),"size":(\d+),"color":"(White|Black|Green|Purple|Blonde|Blue|Red|Orange|Gray|Brown)","material":"(Chlopoc|Leather|Wool|Sintetic|Chlopoc|Len|Rubber)","name":"(.+)","is_for_man":(true|false)},"hat":{"cylinder_height_sm":(\d+),"size":(\d+),"color":"(White|Black|Green|Purple|Blonde|Blue|Red|Orange|Gray|Brown)","material":"(Chlopoc|Leather|Wool|Sintetic|Chlopoc|Len|Rubber)","visor_length_sm":(\d+),"name":"(.+)","is_for_man":(true|false)},"shoes":{"is_shoelaces":(true|false),"size":(\d+),"color":"(White|Black|Green|Purple|Blonde|Blue|Red|Orange|Gray|Brown)","material":"(Chlopoc|Leather|Wool|Sintetic|Chlopoc|Len|Rubber)","outsole_material":"(Chlopoc|Leather|Wool|Sintetic|Chlopoc|Len|Rubber)","name":"(.+)","is_for_man":(true|false)}}
