import styled from 'styled-components';

const StyledButton = styled.button`
    background-color: ${props => {
        if (props.danger) return '#dc3545';
        if (props.success) return '#4caf50';
        return '#007bff';
    }};
    color: white;
    border: none;
    padding: 0.5rem 1rem;
    border-radius: 4px;
    cursor: pointer;
    display: flex;
    align-items: center;
    gap: 0.5rem;
    font-size: 0.9rem;

    &:hover {
        background-color: ${props => {
            if (props.danger) return '#c82333';
            if (props.success) return '#45a049';
            return '#0056b3';
        }};
    }

    &:disabled {
        background-color: #cccccc;
        cursor: not-allowed;
    }
`;

export default StyledButton; 