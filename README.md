# Test for UseTech company

Реализовать приложение Android на Kotlin , которое будет обращаться к API из списка
https://github.com/public-apis/public-apis
Приложение должно состоять из нескольких экранов: 
1. Экран списка элементов с простой разметкой.
2. Экран единичного объекта.
3. Экран поиска по объектам
Плюсом будет реализация поиска как с помощью API, так и с локально.
4. Избранные объекты.
Реализовать добавление/удаление в локальное избранное.
Выбор библиотек и архитектурных решений нужно будет обосновать.

# ----------------------------------

Мной была выбрано API - Google Books API https://developers.google.com/books/
Google Books is our effort to make book content more discoverable on the Web. Using the Google Books API, your application can perform full-text searches and retrieve book information, viewability and eBook availability. You can also manage your personal bookshelves.

Потребуется следующие разрешения:
android.permission.INTERNET
android.permission.ACCESS_NETWORK_STATE
 для доступа в интернет ( https://developer.android.com/training/basics/network-ops/connecting ). Согласно документации не требуется никаких запросов этих разрешений. (Note: Both the Internet and ACCESS_NETWORK_STATE permissions are normal permissions, which means they're granted at install time and don't need to be requested at runtime.)
 

Используем следующие библиотеки com.android.volley:volley - Google Volley для доступа в Интернет через HTTP запросы ( https://www.geeksforgeeks.org/volley-library-in-android/ )
И библиотека Picasso для показа загрузки и показа миниатур com.squareup.picasso ( https://github.com/square/picasso )

Для работы с ассинхронными операциями используется механизм coroutine, поэтому так же подключены библиотеки сопрограм.

Выбор архитектуры приложения будет основан на рекомендациях Google по выбору архитектуры приложения ( https://developer.android.com/topic/architecture )
Внутри класса Application (к которому имеют доступ все activities) стандартно создан обьект фабрики, создающий модель.
UI слой представляет собой набор Activities - основное MainActivity в котором отображается список, окно поиска книг в сервисе для добавления (FindBookActivity) и activity BookDetailsActivity отображающее информацию о нажатом объекте. Оно и добавляет или убирает обьекты в базу как favorite books через ViewModel к данным. Для отображения данных используются RecicleView с адаптерами, поддерживающими фильтрацию (для чего используется SearchView в верху окна).
Data слой представляет собой базу данных SQLite в которой хранятся избранные обьекты, попадающие туда из запросов в Internet.
Слой данных включает в себя работу с базой данных для хранения выбранных книг (favorites) в SQLite базе, окрученой через room и вся работа идет через объект-репозитория (класс BookRepository). В базе хранится только идентификатор книги и ее json-строка, полученная из сервиса.

Все операции с базой данных и запросами в сети выполняются в отдельных корутинах, чтобы не создавать нагрузку на интерфейс пользователя (да и не допустит этого Android). 

Для примера при старте в базу данных наполняется несколько тестовых книг-фаворитов.

Код покрыт тестами с использование JUnit и Espresso (для тестирования визуальных компонетов).
