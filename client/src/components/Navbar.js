import React, { useContext, useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import styled from 'styled-components';
import { Icon } from '@iconify/react';
import homeIcon from '@iconify/icons-mdi/home';
import dashboardIcon from '@iconify/icons-mdi/view-dashboard';
import accountCircleIcon from '@iconify/icons-mdi/account-circle';
import logoutIcon from '@iconify/icons-mdi/logout';
import syncIcon from '@iconify/icons-mdi/sync';
import { AuthContext } from '../context/AuthContext';

const NavbarContainer = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0.5rem 1rem;
  background-color: #007acc;
  color: white;
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1000;
`;

const NavLeft = styled.div`
  display: flex;
  align-items: center;
  flex: 1;
  
  @media (max-width: 768px) {
    width: 100%;
    justify-content: space-between;
  }
`;

const NavItem = styled(Link)`
  margin: 0.5rem 0;
  display: flex;
  align-items: center;
  text-decoration: none;
  color: white;
  & > svg {
    margin-right: 0.5rem;
  }
  &:hover {
    opacity: 0.8;
  }

  @media (max-width: 768px) {
    justify-content: center;
    width: ${props => props.$isUserSection ? 'auto' : '100%'};
    margin: 0.25rem 0;
  }
`;

const NavRight = styled.div`
  display: flex;
  align-items: center;
`;

const UserActions = styled.div`
  display: flex;
  align-items: center;
  margin-left: auto;
`;

const LogoutButton = styled.button`
  background: transparent;
  border: 1px solid white;
  color: white;
  display: flex;
  align-items: center;
  cursor: pointer;
  font-size: 1rem;
  padding: 0.5rem 1rem;
  border-radius: 5px;
  margin-left: 1rem;
  &:hover {
    opacity: 0.8;
  }
  & > svg {
    margin-right: 0.5rem;
  }

  @media (max-width: 768px) {
    margin-left: 0;
    justify-content: center;
    width: fit-content;
  }
`;

const Brand = styled.div`
  display: flex;
  align-items: center;
  font-size: 1.5rem;
  font-weight: bold;
  & > svg {
    margin-left: 0.5rem;
  }
`;

const HamburgerIcon = styled.div`
  display: none;
  font-size: 1.5rem;
  @media (max-width: 768px) {
    display: block;
    cursor: pointer;
  }
`;

const NavbarMenu = styled.div`
  display: flex;
  align-items: center;
  margin-left: 2rem;
  flex: 1;
  justify-content: space-between;
  
  @media (max-width: 768px) {
    display: flex;
    flex-direction: column;
    width: 100%;
    background-color: #007acc;
    position: absolute;
    top: 100%;
    left: 0;
    padding: 0.5rem;
    z-index: 999;
    margin-left: 0;
    align-items: center;
    transform-origin: top;
    transform: scaleY(${({ isOpen }) => (isOpen ? '1' : '0')});
    opacity: ${({ isOpen }) => (isOpen ? '1' : '0')};
    transition: transform 0.3s ease-in-out, opacity 0.2s ease-in-out;
    height: ${({ isOpen }) => (isOpen ? 'auto' : '0')};
  }
`;

const NavSection = styled.div`
  display: flex;
  align-items: center;
  gap: 1rem;

  @media (max-width: 768px) {
    flex-direction: ${props => props.$isUserSection ? 'row' : 'column'};
    align-items: center;
    width: 100%;
    text-align: center;
    gap: 0.25rem;
    ${props => props.$isUserSection && `
      justify-content: center;
    `}
  }
`;

const Navbar = () => {
  const { user, logout } = useContext(AuthContext);
  const navigate = useNavigate();
  const [isMenuOpen, setIsMenuOpen] = useState(false);

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  const toggleMenu = () => {
    setIsMenuOpen(!isMenuOpen);
  };

  return (
    <NavbarContainer>
      <NavLeft>
        <Brand>
          Homesync
        </Brand>
        <NavbarMenu isOpen={isMenuOpen}>
          <NavSection>
            <NavItem to="/dashboard">
              <Icon icon={dashboardIcon} color="white" />
              Dashboard
            </NavItem>
            <NavItem to="/homes">
              <Icon icon={homeIcon} color="white" />
              Homes
            </NavItem>
            {user && user.role === 'ADMINISTRATOR' && (
              <NavItem to="/admin">
                <Icon icon="mdi:shield-account" color="white" />
                Admin Panel
              </NavItem>
            )}
          </NavSection>
          
          <NavSection $isUserSection>
            {user && (
              <NavItem $isUserSection to="/profile">
                <Icon icon={accountCircleIcon} color="white" />
                {user.username}
              </NavItem>
            )}
            <LogoutButton onClick={handleLogout}>
              <Icon icon={logoutIcon} color="white" />
              Logout
            </LogoutButton>
          </NavSection>
        </NavbarMenu>
        <HamburgerIcon onClick={toggleMenu}>
          <Icon icon="mdi:hamburger-menu" color="white" />
        </HamburgerIcon>
      </NavLeft>
    </NavbarContainer>
  );
};

export default Navbar;
