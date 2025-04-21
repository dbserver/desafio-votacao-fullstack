import React from 'react';

interface ButtonProps extends React.ButtonHTMLAttributes<HTMLButtonElement> {
  children: React.ReactNode;
}

export default function Button({ children, ...props }: ButtonProps) {
  return (
    <button
      className="rounded-md border border-green-700 bg-white px-6 py-2 font-semibold text-green-700 transition-colors hover:bg-green-700 hover:text-white"
      {...props}
    >
      {children}
    </button>
  );
}
