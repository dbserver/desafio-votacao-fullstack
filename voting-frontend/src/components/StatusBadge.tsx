type StatusType = "open" | "closed" | "voted" | "pending" | "success" | "error";

interface StatusBadgeProps {
  status: StatusType;
  label: string;
  className?: string;
}

export function StatusBadge({ status, label, className = "" }: StatusBadgeProps) {
  const styles = {
    open: "bg-green-100 text-green-800",
    closed: "bg-gray-100 text-gray-800",
    voted: "bg-blue-100 text-blue-800",
    pending: "bg-yellow-100 text-yellow-800",
    success: "bg-green-100 text-green-800",
    error: "bg-red-100 text-red-800",
  };

  return (
    <span className={`inline-block px-4 py-2 rounded-full text-sm font-semibold ${styles[status]} ${className}`}>
      {label}
    </span>
  );
}
