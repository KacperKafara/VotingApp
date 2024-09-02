import i18next from "i18next";
import { initReactI18next } from "react-i18next";
import Backend from "i18next-http-backend";

let userLang = "en";
const storedLanguage = localStorage.getItem("language");

if (storedLanguage && (storedLanguage === "pl" || storedLanguage === "en")) {
  userLang = storedLanguage;
} else {
  userLang = navigator.language;

  if (userLang !== "pl") {
    userLang = "en";
  }
  localStorage.setItem("language", userLang);
}

i18next
  .use(Backend)
  .use(initReactI18next)
  .init({
    debug: true,
    fallbackLng: userLang,
    interpolation: {
      escapeValue: false,
    }
});

export default i18next;