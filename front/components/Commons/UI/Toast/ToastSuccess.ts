import { addToast } from "@heroui/toast";

export default function ToastSuccess(title: string, description?: string) {
  addToast({
    title,
    description,
    variant: "flat",
    color: "success",
    timeout: 3000,
    shouldShowTimeoutProgress: true,
  });
}
