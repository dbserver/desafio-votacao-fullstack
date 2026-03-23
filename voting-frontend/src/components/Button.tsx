import type { ReactNode, ButtonHTMLAttributes } from "react";

type ButtonVariant = "primary" | "success" | "danger" | "secondary" | "ghost";
type ButtonSize = "sm" | "md" | "lg";

interface ButtonProps extends ButtonHTMLAttributes<HTMLButtonElement> {
  children: ReactNode;
  variant?: ButtonVariant;
  size?: ButtonSize;
  loading?: boolean;
}

export function Button({ 
  children, 
  variant = "primary", 
  size = "md",
  disabled = false,
  loading = false,
  className = "",
  ...props
}: ButtonProps) {
  const variantStyles = {
    primary: "bg-blue-600 hover:bg-blue-700 text-white",
    success: "bg-green-600 hover:bg-green-700 text-white",
    danger: "bg-red-600 hover:bg-red-700 text-white",
    secondary: "bg-gray-200 hover:bg-gray-300 text-gray-800",
    ghost: "bg-transparent hover:bg-gray-100 text-gray-800 border border-gray-300",
  };

  const sizeStyles = {
    sm: "px-3 py-2 text-sm",
    md: "px-6 py-3 text-base",
    lg: "px-8 py-4 text-lg",
  };

  return (
    <button
      {...props}
      disabled={disabled || loading}
      className={`
        ${variantStyles[variant]}
        ${sizeStyles[size]}
        rounded-lg font-semibold transition-all
        disabled:opacity-50 disabled:cursor-not-allowed
        ${className}
      `}
    >
      {loading ? (
        <span className="flex items-center gap-2">
          <span className="animate-spin text-xl">⟳</span>
          Processando...
        </span>
      ) : (
        children
      )}
    </button>
  );
}
