'use client';

import React from 'react';

import { useRouter } from 'next/navigation';
import { IoIosLogOut } from 'react-icons/io';
import { logout } from '@/app/utils/auth';
import { IoArrowBack } from 'react-icons/io5';

export default function Menu() {
  const router = useRouter();

  const handleLogout = () => {
    logout();
    router.replace('/');
  };

  return (
    <nav className="flex items-center justify-between border-b border-green-700 p-6 text-xs font-medium shadow-[3px_3px_4px_1px_rgb(255_255_255/20%),0_0_5px_2px_rgb(0_0_0/40%)] md:text-sm">
      <button
        onClick={() => router.back()}
        aria-label="Retornar a página"
        className="cursor-pointer border-none bg-transparent text-inherit"
      >
        <IoArrowBack size={20} />
      </button>
      <h3 className="font-bold text-green-700">Bem-vindo!</h3>

      <button
        onClick={handleLogout}
        aria-label="Realizar logout"
        className="cursor-pointer border-none bg-transparent text-inherit"
      >
        <IoIosLogOut size={20} />
      </button>
    </nav>
  );
}
