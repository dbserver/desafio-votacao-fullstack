import type { ComponentPropsWithRef } from "react";
import "./Form.css";

export const Form = ({ children, ...props }: ComponentPropsWithRef<"form">) => {
  return (
    <form {...props} className="form" noValidate>
      {children}
    </form>
  );
};

export const FormActions = ({ children }: ComponentPropsWithRef<"div">) => {
  return <div className="form__actions">{children}</div>;
};
