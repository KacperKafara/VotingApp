import i18next from "i18next";
import { create } from "zustand";

interface LanguageStore {
  language?: string;
  setLanguage: (language: string) => void;
}

const LSLanguage = localStorage.getItem("language");

export const useLanguageStore = create<LanguageStore>((set) => ({
  language: LSLanguage === null ? undefined : LSLanguage,
  setLanguage: (newLanguage: string) =>
    set(({ language }) => {
      if (language === newLanguage) {
        return { language };
      }
      if (newLanguage !== "en" && newLanguage !== "pl") {
        localStorage.setItem("language", "pl");
        i18next.changeLanguage("pl");
        return { language: "pl" };
      }
      localStorage.setItem("language", newLanguage);
      i18next.changeLanguage(newLanguage);
      return { language: newLanguage };
    }),
}));