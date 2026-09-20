import type { ComponentPropsWithRef } from "react";
import "./TextField.css";

type TextFieldProps = ComponentPropsWithRef<"input"> & {
  label: string;
  error?: string;
};

export const TextField = ({ label, error, id, ...props }: TextFieldProps) => {
  const fieldId = id ?? props.name;
  const errorId = `${fieldId}-error`;

  return (
    <div className="text-field">
      <label className="text-field__label" htmlFor={fieldId}>
        {label}
      </label>

      <input
        {...props}
        id={fieldId}
        className={error ? "text-field__input text-field__input--error" : "text-field__input"}
        aria-invalid={Boolean(error)}
        aria-describedby={error ? errorId : undefined}
      />

      {error && (
        <span className="text-field__error" id={errorId} role="alert">
          {error}
        </span>
      )}
    </div>
  );
};
