export interface RegistrationData {
  username: string;
  password: string;
  email: string;
  firstName: string;
  lastName: string;
  phoneNumber: string;
  birthDate: Date;
  gender: number;
  language: string;
}

export interface FillOAuthData {
  jwtToken: string;
  username: string;
  email: string;
  firstName: string;
  lastName: string;
  phoneNumber: string;
  birthDate: Date;
  gender: number;
}
