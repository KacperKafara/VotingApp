import { FC, useEffect, useState } from 'react';
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
} from './ui/dialog';
import useAxiosPrivate from '@/data/useAxiosPrivate';
import { TFunction } from 'i18next';

interface QRCodeDialogProps {
  open: boolean;
  onOpenChange: () => void;
  tFunction: TFunction;
}

const QRCodeDialog: FC<QRCodeDialogProps> = ({
  open,
  onOpenChange,
  tFunction: t,
}) => {
  const [qrCodeSrc, setQrCodeSrc] = useState('');
  const { api } = useAxiosPrivate();

  useEffect(() => {
    const fetchQRCode = async () => {
      const { data } = await api.get('/qr-code', {
        responseType: 'arraybuffer',
      });

      const base64 = btoa(
        new Uint8Array(data).reduce(
          (data, byte) => data + String.fromCharCode(byte),
          ''
        )
      );

      setQrCodeSrc(`data:image/png;base64,${base64}`);
    };

    if (open) {
      fetchQRCode();
    }
  }, [api, open]);

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>QR Code</DialogTitle>
          <DialogDescription>{t('scanThisCode')}</DialogDescription>
        </DialogHeader>
        <div className="flex justify-center">
          <img src={qrCodeSrc} alt="QR Code" />
        </div>
      </DialogContent>
    </Dialog>
  );
};

export default QRCodeDialog;
