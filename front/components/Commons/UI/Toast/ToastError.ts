import { addToast } from "@heroui/toast";

export default function ToastError(title: string, description?: string) {
  addToast({
    title,
    description,
    variant: "flat",
    color: "danger",
    timeout: 3000,
    shouldShowTimeoutProgress: true,
  });
}
