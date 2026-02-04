# Ставротека (Stavropol Places App)

Гид по Ставропольскому краю: места, где поесть, афиша, знаменитые земляки и избранное. Проект постепенно мигрирует на Jetpack Compose с единой навигацией через `MainActivity`.

## Возможности
- Места: список, поиск, карточка места.
- Где поесть: список, карточка ресторана с фото и графиком работы.
- Афиша (webview-экран/заглушка).
- Земляки: список и карточка персоны.
- Избранное.

## Технологии и инструменты
- **Kotlin**, **Android SDK 34**, **Java 17**
- **Jetpack Compose** + **Material 3** (основные экраны)
- **Navigation Compose** (single-activity навигация)
- **Hilt** (DI)
- **Coil** (изображения в Compose) + **Glide** (legacy/RecyclerView)
- **Gson** (JSON парсинг)
- **Google Maps/Places SDK** (подготовлено, используется точечно)

## Архитектура
- Гибрид: часть экранов на Compose, часть — legacy XML.
- Единая навигация через `MainActivity` + `NavHost`.
- Данные сейчас локальные, из `app/src/main/assets/*.json`.

## Структура проекта (ключевые каталоги)
```
app/src/main/java/com/example/stavropolplacesapp/
├─ data/                 # источники/репозитории данных
├─ di/                   # Hilt модули
├─ navigation/           # маршруты и навигационные хелперы
├─ presentation/         # Compose-экраны
├─ places/, eat/, famous_people/  # домены/legacy-экраны
├─ ui/                   # общие UI-компоненты и тема
└─ utils/                # утилиты
```

## Данные
Данные хранятся в assets:
- `places.json`
- `eat_place.json`
- `people.json`
- `region.json`

## Сборка и запуск
Требования:
- Android Studio (Arctic Fox+), JDK 17
- Android SDK 34

Команды:
```bash
./gradlew assembleDebug
```

Установка на устройство:
```bash
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

## Тесты
```bash
./gradlew testDebugUnitTest
```

## CI/CD
GitHub Actions: `.github/workflows/android.yml`
- сборка debug
- unit-тесты

## Примечания
- Firebase временно исключён (не используется).
- Идёт миграция на Compose: часть экранов всё ещё legacy, но навигация унифицирована через `MainActivity`.

## Roadmap (кратко)
- Полный перевод экранов на Compose
- Упрощение навигации (удаление legacy Activity)
- Улучшение карточек и фото-галерей
- Расширение данных и персонализация

---
Если нужно, добавлю диаграммы архитектуры/модули, подробное описание API и формат данных.
