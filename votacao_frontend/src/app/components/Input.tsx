import React from 'react';
import { UseFormRegisterReturn } from 'react-hook-form';

interface InputProps
  extends Omit<React.InputHTMLAttributes<HTMLInputElement>, 'name'> {
  field: UseFormRegisterReturn;
}

export default function Input({ field, ...props }: InputProps) {
  return (
    <input
      className="focus:transparent mt-2 w-full rounded-md border border-gray-300 bg-white px-4 py-2 text-gray-800 placeholder-gray-400 transition-colors focus:border-green-700 focus:text-gray-800 focus:placeholder-white focus:outline-none"
      {...field}
      {...props}
    />
  );
}
