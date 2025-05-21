import { type INavbarTabs } from '@constants/navbarTabs';
import { ListItemButton, ListItemText, Collapse, List } from '@mui/material';
import { ExpandLess, ExpandMore } from '@mui/icons-material';
import { useState } from 'react';
import NavTab from '../NavTab';

interface NavAccordionProps {
  tab: INavbarTabs;
}

const NavAccordion = ({ tab }: NavAccordionProps) => {
  const [isMenuCollapsed, setIsMenuCollapsed] = useState(false);

  const toggleMenuCollapsed = () => {
    setIsMenuCollapsed(!isMenuCollapsed);
  };

  return (
    <li key={tab.label}>
      <ListItemButton onClick={toggleMenuCollapsed}>
        <ListItemText primary={tab.label} />
        {isMenuCollapsed ? <ExpandLess /> : <ExpandMore />}
      </ListItemButton>
      <Collapse in={isMenuCollapsed} timeout="auto" unmountOnExit>
        <List disablePadding>{tab.subItens?.map((tab) => <NavTab tab={tab} pl={4} py={0.5} key={tab.label} />)}</List>
      </Collapse>
    </li>
  );
};

export default NavAccordion;
