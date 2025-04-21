import React from 'react';

interface LabelProps extends React.LabelHTMLAttributes<HTMLLabelElement> {
  children: React.ReactNode;
  isRequired?: boolean;
  text: string;
}

export default function Label({
  children,
  isRequired = false,
  text,
}: LabelProps) {
  return (
    <label className="mb-2 block text-sm font-medium text-gray-700">
      {text}
      {isRequired && <span className="font-bold text-red-700">*</span>}
      {children}
    </label>
  );
}
