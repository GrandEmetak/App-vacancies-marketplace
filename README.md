# App Vacancies Marketplace

[![Build Status](https://travis-ci.com/Halsyon/App-vacancies-marketplace.svg?branch=main)](https://travis-ci.com/Halsyon/App-vacancies-marketplace)
[![codecov](https://codecov.io/gh/Halsyon/App-vacancies-marketplace/branch/main/graph/badge.svg?token=55F8GZ1R8S)](https://codecov.io/gh/Halsyon/App-vacancies-marketplace)
![GitHub top language](https://img.shields.io/github/languages/top/Halsyon/App-vacancies-marketplace?logo=java&logoColor=red)
![GitHub last commit](https://img.shields.io/github/last-commit/Halsyon/App-vacancies-marketplace?logo=github)

Приложение — Маркетплейс вакансий.

Описание.

Система запускается по расписанию. 

Период запуска указывается в настройках - app.properties.

Первый сайт будет sql.ru. В нем есть раздел job. 

Программа должна считывать все вакансии относящиеся к Java и записывать их в базу.

Доступ к интерфейсу будет через REST API.

В проекте агрегатор будет использоваться база данных.

Расширение.

1. В проект можно добавить новые сайты без изменения кода.

2. В проекте можно сделать параллельный парсинг сайтов.

Приложение будет собираться в jar.
