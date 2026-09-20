import type { PropsWithChildren } from "react";
import "./Card.css";

type CardProps = PropsWithChildren<{
  title?: string;
}>;

export const Card = ({ title, children }: CardProps) => {
  return (
    <section className="card">
      {title && <h2 className="card__title">{title}</h2>}
      {children}
    </section>
  );
};
