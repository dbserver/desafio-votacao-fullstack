import React from 'react';
import { UseFormRegisterReturn } from 'react-hook-form';

interface SelectProps
  extends Omit<React.SelectHTMLAttributes<HTMLSelectElement>, 'name'> {
  field: UseFormRegisterReturn;
  options: { value: string | number; label: string }[];
  required?: boolean;
}

export default function Select({
  field,
  options,
  required,
  ...props
}: SelectProps) {
  return (
    <select
      className="focus:transparent mt-2 w-full rounded-md border border-gray-300 bg-white px-4 py-2 text-gray-800 placeholder-gray-400 transition-colors focus:border-green-700 focus:text-gray-800 focus:outline-none"
      {...field}
      {...props}
      required={required}
    >
      <option value="" disabled selected>
        Selecione uma opção
      </option>
      {options.map((option) => (
        <option key={option.value} value={option.value}>
          {option.label}
        </option>
      ))}
    </select>
  );
}
