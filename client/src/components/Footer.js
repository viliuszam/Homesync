import React from 'react';
import styled from 'styled-components';
import { Icon } from '@iconify/react';
import heartIcon from '@iconify/icons-mdi/university';

const FooterContainer = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 1rem;
  background-color: #007acc;
  color: white;
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  z-index: 1000;
`;

const FooterText = styled.div`
  font-size: 1rem;
`;

const Footer = () => {
  return (
    <FooterContainer>
      <FooterText>Created by Vilius Z. 2024 <Icon icon={heartIcon} color="#FFFFFF" width="16" height="16" /></FooterText>
      <></>
    </FooterContainer>
  );
};

export default Footer;