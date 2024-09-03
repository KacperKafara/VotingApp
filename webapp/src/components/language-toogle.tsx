import { Button } from "@/components/ui/button"
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu"
import { useLanguageStore } from "@/i18n/languageStore"
import { useTranslation } from "react-i18next";

export function LanguageToogle() {
  const { setLanguage } = useLanguageStore();
  const { t } = useTranslation("common");

  return (
    <DropdownMenu>
      <DropdownMenuTrigger asChild>
        <Button variant="outline" className="w-3/5">
          {t("languageSelector")}
        </Button>
      </DropdownMenuTrigger>
      <DropdownMenuContent align="center">
        <DropdownMenuItem className="hover:cursor-pointer" onClick={() => setLanguage("pl")}>
          Polski
        </DropdownMenuItem>
        <DropdownMenuItem className="hover:cursor-pointer" onClick={() => setLanguage("en")}>
          English
        </DropdownMenuItem>
      </DropdownMenuContent>
    </DropdownMenu>
  )
}
