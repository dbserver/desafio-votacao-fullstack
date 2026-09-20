import type { ComponentPropsWithRef } from "react";
import "./Button.css";

type ButtonProps = ComponentPropsWithRef<"button"> & {
  variant?: "primary" | "secondary";
  loading?: boolean;
};

export const Button = ({
  variant = "primary",
  loading = false,
  disabled,
  children,
  ...props
}: ButtonProps) => {
  return (
    <button
      {...props}
      className={`button button--${variant}`}
      disabled={disabled || loading}
      aria-busy={loading}
    >
      {loading ? "Enviando..." : children}
    </button>
  );
};
