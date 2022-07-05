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
 для доступа в интернет ( https://developer.android.com/training/basics/network-ops/connecting )
 

Используем следующие библиотеки com.android.volley:volley - Google Volley для доступа в Интернет через HTTP запросы ( https://www.geeksforgeeks.org/volley-library-in-android/ )
И библиотека Picasso для показа загрузки и показа миниатур com.squareup.picasso ( https://github.com/square/picasso )


