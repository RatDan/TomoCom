

#**Wymagany plik google-services.json**

Aplikacja Android wymaga pliku google-services.json, aby poprawnie działać. Plik ten jest niezbędny do korzystania z usług Firebase, takich jak autoryzacja, analityka czy powiadomienia push.

#**Instrukcja dodania pliku:**

Pobierz plik google-services.json z konsoli Firebase.

Umieść go w katalogu app projektu Android:

/projekt-android/
├── app/
│   ├── google-services.json  <-- TUTAJ
│   ├── src/
│   ├── build.gradle
│   ├── ...

Upewnij się, że plik nie jest dodany do systemu kontroli wersji (np. .gitignore).

Uruchom ponownie projekt i skompiluj aplikację.

#**Ważne**

Bez pliku google-services.json aplikacja nie uruchomi się poprawnie lub może generować błędy związane z Firebase.

