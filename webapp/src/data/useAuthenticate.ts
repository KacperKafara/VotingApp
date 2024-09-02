import { useMutation } from "@tanstack/react-query";
import { api } from "./api";
import { AxiosError } from "axios";
import { useToast } from "@/hooks/use-toast";

interface LoginRequest {
  username: string;
  password: string;
}

interface LoginResponse {
  token: string;
}

export const useAuthenticate = () => {
  const { toast } = useToast();

  const { mutateAsync, isSuccess, isPending } = useMutation({
    mutationFn: async (data: LoginRequest) => {
      const response = await api.post<LoginResponse>("/authenticate", data);
      return response.data;
    },
    onError: (error: AxiosError) => {
      toast({
        variant: "destructive",
        title: "Authentication failed",
        description: error.response?.data?.exceptionCode,
      });
    }
  });

  return { authenticate: mutateAsync, isSuccess, isPending };
};