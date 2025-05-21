import { List, useMediaQuery, useTheme } from '@mui/material';
import navbarTabs, { type INavbarTabs } from '@constants/navbarTabs';
import NavAccordion from './NavAccordion';
import NavTab from './NavTab';
import { StyledDrawer } from './styles';

interface INavbar {
  isDrawerOpen: boolean;
  handleDrawerToggle: () => void;
}

const Navbar = ({ isDrawerOpen, handleDrawerToggle }: INavbar) => {
  const { breakpoints } = useTheme();
  const isSmallScreen = useMediaQuery(breakpoints.down('sm'));

  return (
    <StyledDrawer
      open={isDrawerOpen}
      onClose={handleDrawerToggle}
      variant={isSmallScreen ? 'temporary' : 'permanent'}
      ModalProps={{
        keepMounted: true,
      }}
    >
      <nav style={{ flexGrow: 1 }}>
        <List>
          {navbarTabs.map((tab: INavbarTabs) => {
            if (!tab?.subItens) return <NavTab tab={tab} key={tab.label} />;
            return <NavAccordion tab={tab} key={tab.label} />;
          })}
        </List>
      </nav>
    </StyledDrawer>
  );
};

export default Navbar;
