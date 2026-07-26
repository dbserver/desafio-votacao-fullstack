import { useEffect, useState } from "react";

export function useNow(intervalo = 1000) {
  const [now, setNow] = useState(() => Date.now());

  useEffect(() => {
    const intervalId = window.setInterval(() => {
      setNow(Date.now());
    }, intervalo);

    return () => window.clearInterval(intervalId);
  }, [intervalo]);

  return now;
}
