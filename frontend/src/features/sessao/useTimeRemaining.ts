import { useEffect, useState } from "react";

const secondsUntil = (end: string) => {
  const remaining = new Date(end).getTime() - Date.now();

  return Math.max(0, Math.floor(remaining / 1000));
};

const twoDigits = (value: number) => String(value).padStart(2, "0");

const formatTime = (seconds: number) => {
  const hours = Math.floor(seconds / 3600);
  const minutes = Math.floor((seconds % 3600) / 60);
  const rest = seconds % 60;

  if (hours > 0) {
    return `${hours}h ${twoDigits(minutes)}min`;
  }

  return `${twoDigits(minutes)}:${twoDigits(rest)}`;
};

export const useTimeRemaining = (end: string | undefined) => {
  const [, setTick] = useState(0);

  useEffect(() => {
    if (!end) {
      return;
    }

    const interval = setInterval(() => {
      setTick((tick) => tick + 1);

      if (secondsUntil(end) === 0) {
        clearInterval(interval);
      }
    }, 1000);

    return () => clearInterval(interval);
  }, [end]);

  const seconds = end ? secondsUntil(end) : 0;

  return {
    seconds,
    isOpen: seconds > 0,
    text: formatTime(seconds),
  };
};
