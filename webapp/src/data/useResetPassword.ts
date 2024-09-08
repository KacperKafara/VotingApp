import { useToast } from "@/hooks/use-toast";
import { useMutation } from "@tanstack/react-query";
import { useTranslation } from "react-i18next";
import { api } from "./api";
import { AxiosError } from "axios";
import { ApplicationError } from "@/types/applicationError";
import { useNavigate } from "react-router-dom";


interface ResetPasswordRequest {
  email: string;
  language: string;
}

export const useResetPassword = (onOpenChange: () => void) => {
  const { toast } = useToast();
  const { t } = useTranslation(['errors', 'resetPassword']);

  const { mutateAsync, isSuccess, isPending } = useMutation({
    mutationFn: async (data: ResetPasswordRequest) => {
      const response = await api.post<unknown>("/resetPassword", data);
      return response.data;
    },
    onError: (error: AxiosError) => {
      toast({
        variant: "destructive",
        title: t('errors:defaultTitle'),
        description: t('errors:' + (error.response?.data as ApplicationError).code)
      });
    },
    onSuccess: () => {
      onOpenChange();
      toast({
        title: t('resetPassword:successTitle'),
        description: t('resetPassword:successDescription'),
      })
    }
  });

  return { resetPassword: mutateAsync, isSuccess, isPending };
};

export const useVerifyToken = () => {
  const { toast } = useToast();
  const { t } = useTranslation(['errors', 'resetPassword']);
  const navigate = useNavigate();

  const { mutateAsync, isSuccess } = useMutation({
    mutationFn: async (token: string) => {
      const response = await api.post<unknown>(`/resetPassword/${token}/verify`);
      return response.data;
    },
    onError: () => {
      navigate('/');
      toast({
        variant: "destructive",
        title: t('errors:defaultTitle'),
        description: t('resetPassword:errors.tokenUsed')
      });
    },
  });

  return { verifyToken: mutateAsync, isSuccess };
}

export const useChangePassword = () => {
  const { toast } = useToast();
  const { t } = useTranslation(['errors', 'resetPassword']);
  const navigate = useNavigate();

  const { mutateAsync, isSuccess, isPending } = useMutation({
    mutationFn: async (data : { password: string, token: string }) => {
      const response = await api.post<unknown>(`/resetPassword/${data.token}`, { password: data.password });
      return response.data;
    },
    onError: (error: AxiosError) => {
      toast({
        variant: "destructive",
        title: t('errors:defaultTitle'),
        description: t('errors:' + (error.response?.data as ApplicationError).code)
      });
    },
    onSuccess: () => {
      navigate('/');
      toast({
        title: t('resetPassword:successTitle'),
        description: t('resetPassword:successChangePassword'),
      });
    }
  });

  return { changePassword: mutateAsync, isSuccess, isPending };
}