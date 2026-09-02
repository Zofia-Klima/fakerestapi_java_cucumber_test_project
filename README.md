# Założenia projektu

Projekt został stworzony na podstawie wytycznych:

Korzystając z publicznego API dostępnego pod adresem https://fakerestapi.azurewebsites.net/index.html, napisz test w Javie. Wszystkie operacje na API muszą być wykonane z poziomu kodu Java, a nie bezpośrednio przez interfejs Swagger.

Wymagania:

1. W czterech równoległych wątkach utwórz czterech autorów (jeden autor na wątek).

2. Zweryfikuj za pomocą API, że wszyscy autorzy zostali poprawnie utworzeni.

3. Następnie otwórz UI tej samej strony i porównaj autorów wyświetlanych w UI z danymi pobranymi z API.

4. Przygotuj raport podsumowujący wyniki.

5. Dla lepszej czytelności i struktury rozważ użycie Cucumbera.

## Wymagania

Projekt wymaga zainstalowanej Javy i Google Chrome.

Biblioteki zewnętrzne znajdują się w katalogu `lib`.

Są to m.in.:

- Gson
- Selenium WebDriver
- zależności wymagane przez Selenium
- Cucumber
- zależności wymagane przez Cucumber

## Kompilacja

Aby skompilować projekt w systemie Windows, należy wykonać:

```powershell
javac -cp ".;lib/*;lib/cucumber/*" Author.java AuthorThread.java AuthorApiClient.java AuthorUiTest.java AuthorSteps.java
```

## Uruchomienie

Aby uruchomić wszystkie scenariusze:

```powershell
java -ea -cp ".;lib/*;lib/cucumber/*" io.cucumber.core.cli.Main features/authors.feature
```

Opcja `-ea` włącza asercje Javy.

## Struktura projektu

- `AuthorSteps.java` - definicje kroków Cucumbera oraz asercje wykorzystywane w scenariuszach testowych.
- `Author.java` - model danych autora.
- `AuthorThread.java` - obsługuje operacje POST dla pojedynczego autora wykonywane w osobnym wątku oraz GET autora po ID.
- `AuthorApiClient.java` - pobiera listę wszystkich autorów bezpośrednio z API.
- `AuthorUiTest.java` - obsługuje Swagger UI za pomocą Selenium i pobiera dane autorów wyświetlone w UI.
- `features/authors.feature` - zawiera scenariusze testowe zapisane w języku Gherkin.
- `lib/` - zawiera Gson, Selenium WebDriver i wymagane zależności.
- `lib/cucumber/` - zawiera biblioteki wymagane do uruchomienia Cucumbera bez użycia Mavena.
- `docs/Raport_z_testow_FakeRESTApi_Cucumber_version.pdf` - zawiera raport z testów.

## Selenium

Prosty kod Selenium wykorzystany jest z własnej inicjatywy ze względu na punkt 3 zadania:
>(...)
>3. Następnie otwórz UI tej samej strony i porównaj autorów wyświetlanych w UI z danymi pobranymi z API.

Domyślam się, że w zamyśle nie było otworzenie UI przy użyciu Selenium, jednak zrobiłam to w ramach przetestowania jak działa Selenium w Javie.

Kod zawarty w pliku AuthorUiTest.java otwiera stronę https://fakerestapi.azurewebsites.net/index.html, rozwija endpoint GET /api/v1/Authors, następnie klika w przyciski "Try it out" oraz "Execute", oraz zapisuje wyświetlone ciało odpowiedzi w zmiennej responseBody.

Dzięki temu mogę porównać dane pobrane bezpośrednio przez żądanie GET z danymi zwróconymi przez ten sam endpoint uruchomiony w Swagger UI.

## Dodatkowe uwagi

Komentarze w kodzie zostały napisane w języku angielskim, w celu zachowania dobrej praktyki oraz spójności w kodzie.
Projekt zawiera wersję przekształconą do struktury Cucumbera.