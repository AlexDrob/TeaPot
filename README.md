**TeaPot - это приложение для управления Wi-Fi чайником, который был мной сделан на ESP8266**

Приложение совместимо с Android 4.0 (API 14) и выше.

# Описание основного функционала
Для управления необходимо, чтобы телефон Android находился в одной Wi-Fi сети с чайником. В случае если Wi-Fi выключен или сеть отличается от ожидаемой, приложение запустится в демонстрационном режиме.

![Image](/screenshots/Screenshot_2017-03-22-14-40-34.png)

На главном экране можно установить один из трех режимов работы чайника:
- ВЫКЛ: чайник выключен
- АВТО: поддерживать установленную температуру воды в чайнике
- НАГР: закипятить воду в чайнике и затем перейти на режим АВТО
а также установить температуру режима АВТО и посмотреть текущую температуру воды в чайнике.

![Image](/screenshots/Screenshot_2017-03-22-14-35-29.png)
![Image](/screenshots/Screenshot_2017-03-22-14-37-39.png)
![Image](/screenshots/Screenshot_2017-03-22-14-37-59.png)

Синхронизация данных с Wi-Fi чайником осуществляется путем отправки широковещательных UDP запросов каждые 10 секунд. Таким образом, была решена проблема отслеживания смены ip-адреса чайника.
Изменение режима работы и температуры режима АВТО выполняется при помощи отправки TCP сообщения. В случае проблем выдается toast-оповещение

![Image](/screenshots/Screenshot_2017-03-22-14-37-21.png)

# Дополнительный функционал и вкладки
![Image](/screenshots/Screenshot_2017-03-22-14-35-35.png)

## Домой 
Возврат на главный экран установки температуры и режимов

## Настройка Wi-Fi
Позволяет изменить имя Wi-Fi сети, в которой находится чайник, а также увидеть текущий ip-адрес чайника

![Image](/screenshots/Screenshot_2017-03-22-14-35-40.png)

## Управление температурой
Позволяет установить минимальный и максимальный пределы ниже и выше которых нельзя установить температуру режима АВТО

![Image](/screenshots/Screenshot_2017-03-22-14-35-45.png)
![Image](/screenshots/Screenshot_2017-03-22-14-35-56.png)

## Уведомления
Позволяет выбрать события при наступлении которых пользователь желает получать оповещения. Также позволяет выбрать тип оповещения.
Для корректной работы оповещений реализован foreground сервис 

![Image](/screenshots/Screenshot_2017-03-22-14-36-01.png)
![Image](/screenshots/Screenshot_2017-03-22-14-38-25.png)

## Цветовые темы
Позволяет изменить цветовую тему приложения

![Image](/screenshots/Screenshot_2017-03-22-14-36-10.png)
![Image](/screenshots/Screenshot_2017-03-22-14-36-12.png)
![Image](/screenshots/Screenshot_2017-03-22-14-36-15.png)

## Отправить данные
Отправляет на мой электронный адрес текущие данные с чайника 

## Выход
Выход из приложения с остановкой foreground сервиса
