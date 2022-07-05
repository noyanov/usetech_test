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



Мной была выбрано API - Google Books API https://developers.google.com/books/

Google Books is our effort to make book content more discoverable on the Web. Using the Google Books API, your application can perform full-text searches and retrieve book information, viewability and eBook availability. You can also manage your personal bookshelves.

Потребуется следующие разрешения:
android.permission.INTERNET
android.permission.ACCESS_NETWORK_STATE
 для доступа в интернет ( https://developer.android.com/training/basics/network-ops/connecting ). Согласно документации не требуется никаких запросов этих разрешений. (Note: Both the Internet and ACCESS_NETWORK_STATE permissions are normal permissions, which means they're granted at install time and don't need to be requested at runtime.)
 

Используем следующие библиотеки com.android.volley:volley - Google Volley для доступа в Интернет через HTTP запросы ( https://www.geeksforgeeks.org/volley-library-in-android/ )
И библиотека Picasso для показа загрузки и показа миниатур com.squareup.picasso ( https://github.com/square/picasso )

Выбор архитектуры приложения будет основан на рекомендациях Google по выбору архитектуры приложения ( https://developer.android.com/topic/architecture )
UI слой представляет собой Activity - основное MainActivity в котором отображается список и второе activity BookDetailsActivity отображающее информацию о нажатом объекте.
Presenter слой использует ViewModel
Data слой представляет собой базу данных SQLite в которой хранятся избранные обьекты, попадающие туда из запросов в Internet
