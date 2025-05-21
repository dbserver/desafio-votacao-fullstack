import { type INavbarTabs } from '@constants/navbarTabs';
import { ListItem, ListItemButton, ListItemText } from '@mui/material';
import { useLocation, useNavigate } from 'react-router-dom';
import { COLOR_BLACK, COLOR_PRIMARY, COLOR_WHITE } from '@constants/colors';
import { memo } from 'react';

interface NavTabProps {
  tab: INavbarTabs;
  pl?: number;
  py?: number;
}

const NavTab = ({ tab, pl = 2, py = 1 }: NavTabProps) => {
  const navigate = useNavigate();
  const { pathname } = useLocation();

  return (
    <ListItem
      key={tab.label}
      disablePadding
      sx={{
        borderRadius: '4px',
        backgroundColor: tab.route === pathname ? COLOR_PRIMARY : COLOR_WHITE,
      }}
    >
      <ListItemButton
        sx={{
          pl,
          py,
          borderRadius: '4px',
          backgroundColor: tab.route === pathname ? COLOR_PRIMARY : COLOR_WHITE,
        }}
        onClick={() => {
          if (tab?.route) return navigate(tab.route);
        }}
      >
        <ListItemText
          primary={tab.label}
          disableTypography
          sx={{
            color: tab.route === pathname ? COLOR_WHITE : COLOR_BLACK,
            fontWeight: tab.route === pathname ? 600 : 400,
          }}
        />
      </ListItemButton>
    </ListItem>
  );
};

export default memo(NavTab);
