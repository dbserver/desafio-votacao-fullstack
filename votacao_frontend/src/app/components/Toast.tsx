import { useEffect, useState } from 'react';

type TToastProps = {
  message: string;
  onClose: () => void;
  type: string;
};

export enum ToastTypeEnum {
  ERROR = 'error',
  SUCCESS = 'success',
}

export const Toast = ({
  message,
  onClose,
  type = ToastTypeEnum.SUCCESS,
}: TToastProps) => {
  const [visible, setVisible] = useState(true);

  const bgColor = type === ToastTypeEnum.ERROR ? 'bg-red-700' : 'bg-green-700';

  useEffect(() => {
    const timer = setTimeout(() => {
      setVisible(false);
      onClose();
    }, 3000);

    return () => clearTimeout(timer);
  }, []);

  if (!visible) return null;

  return (
    <div
      className={`fixed top-4 right-4 z-10 animate-[fade-in_0.3s_ease-in-out] rounded-lg px-6 py-4 text-sm text-white shadow-lg ${bgColor}`}
    >
      {message}
    </div>
  );
};
