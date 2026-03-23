interface LoadingSpinnerProps {
  size?: "sm" | "md" | "lg";
  message?: string;
}

export function LoadingSpinner({ size = "md", message }: LoadingSpinnerProps) {
  const sizes = {
    sm: "w-4 h-4",
    md: "w-8 h-8",
    lg: "w-12 h-12",
  };

  return (
    <div className="flex flex-col items-center justify-center gap-4 py-8">
      <div className={`${sizes[size]} border-4 border-gray-300 border-t-blue-500 rounded-full animate-spin`} />
      {message && <p className="text-gray-600">{message}</p>}
    </div>
  );
}
