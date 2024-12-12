import React from 'react';
import styled from 'styled-components';
import PageWithNavbar from '../components/PageWithNavbar';

const Container = styled.div`
    padding: 2rem;
`;

const DashboardPage = () => {
    return (
        <>
        <PageWithNavbar>
            <Container>
                <h1>Welcome to the Dashboard</h1>
                <p> Application created for the Kaunas University of Technology Website design module </p>
            </Container>
        </PageWithNavbar>
        </>
    );
};

export default DashboardPage;