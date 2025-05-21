import MenuIcon from '@mui/icons-material/Menu';
import { IconButton, Toolbar } from '@mui/material';
import { StyledAppBar } from './styles';

interface HeaderProps {
  handleDrawerToggle: () => void;
}

const Header = ({ handleDrawerToggle }: HeaderProps) => {
  return (
    <StyledAppBar>
      <Toolbar
        sx={{
          display: 'flex',
          justifyContent: 'start',
        }}
      >
        <IconButton aria-label="abrir menu" edge="start" onClick={handleDrawerToggle}>
          <MenuIcon />
        </IconButton>
      </Toolbar>
    </StyledAppBar>
  );
};

export default Header;
