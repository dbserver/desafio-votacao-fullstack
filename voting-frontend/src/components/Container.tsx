import type { ReactNode } from "react";

interface ContainerProps {
  children: ReactNode;
}

export function Container({ children }: ContainerProps) {
  return (
    <div className="bg-white p-8 rounded-2xl shadow-md">
      {children}
    </div>
  );
}