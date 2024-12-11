import React from 'react';
import Navbar from './Navbar';
import Footer from './Footer';

const PageWithNavbar = ({ children }) => {
  return (
    <>
      <Navbar />
      <div className="content-with-navbar" style={{ paddingBottom: '60px', paddingTop: '60px' }}>
        {children}
      </div>
      <Footer />
    </>
  );
};

export default PageWithNavbar;