"use client";
import { Button } from "@heroui/button";
import { useTheme } from "next-themes";
import { TbMoon, TbSunFilled } from "react-icons/tb";
import { useEffect, useState } from "react";

export default function ThemeButton() {
  const [currentTheme, setCurrentTheme] = useState<string>("light");
  const { theme, setTheme } = useTheme();

  useEffect(() => {
    if (theme) setCurrentTheme(theme);
  }, [theme]);

  return (
    <Button
      isIconOnly
      variant={"flat"}
      onPress={() => setTheme(theme === "dark" ? "light" : "dark")}
    >
      {currentTheme === "dark" ? (
        <TbMoon size={18} />
      ) : (
        <TbSunFilled size={18} />
      )}
    </Button>
  );
}
