import { FC } from "react";
import { useNavigate } from "react-router-dom";
import { Button } from "./ui/button";
import { useTranslation } from "react-i18next";
import { useUserStore } from "@/store/userStore";


const LogoutButton: FC = () => {
  const navigate = useNavigate();
  const { t } = useTranslation("common");
  const { clearToken, clearActiveRole } = useUserStore();

  const handleOnClick = () => {
    clearToken();
    clearActiveRole();
    navigate("/");
  }


  return (
    <>
      <Button className="w-3/5" onClick={handleOnClick}>{t('logout')}</Button>
    </>
  );

}

export default LogoutButton;