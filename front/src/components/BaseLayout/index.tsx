import { useState } from 'react';
import { Outlet } from 'react-router-dom';
import Header from '@components/Header';
import Navbar from '@components/Navbar';
import { MainContainer, PageContainer } from './styles';

const BaseLayout = () => {
  const [isDrawerOpen, setIsDrawerOpen] = useState(false);

  const handleDrawerToggle = () => {
    setIsDrawerOpen(!isDrawerOpen);
  };

  return (
    <>
      <Header handleDrawerToggle={handleDrawerToggle} />
      <Navbar isDrawerOpen={isDrawerOpen} handleDrawerToggle={handleDrawerToggle} />
      <MainContainer>
        <PageContainer>
          <Outlet />
        </PageContainer>
      </MainContainer>
    </>
  );
};

export default BaseLayout;
