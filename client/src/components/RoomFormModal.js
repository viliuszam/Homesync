import React, { useState } from 'react';
import styled from 'styled-components';
import { formatEnumValue } from '../utils/formatters';

const ModalContent = styled.div`
    background-color: white;
    padding: 2rem;
    border-radius: 8px;
    width: 90%;
    max-width: 500px;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.2);
`;

const FormGroup = styled.div`
    margin-bottom: 1rem;
`;

const Label = styled.label`
    display: block;
    margin-bottom: 0.5rem;
    font-weight: bold;
`;

const Input = styled.input`
    width: 100%;
    padding: 0.5rem;
    border: 1px solid #ddd;
    border-radius: 4px;
`;

const Select = styled.select`
    width: 100%;
    padding: 0.5rem;
    border: 1px solid #ddd;
    border-radius: 4px;
`;

const ErrorMessage = styled.div`
    color: #dc3545;
    font-size: 0.875rem;
    margin-top: 0.25rem;
`;

const ModalActions = styled.div`
    display: flex;
    justify-content: flex-end;
    gap: 1rem;
    margin-top: 1rem;
`;

const ServerError = styled.div`
    background-color: #fff3f3;
    color: #dc3545;
    padding: 1rem;
    border-radius: 4px;
    margin-bottom: 1rem;
    border: 1px solid #dc3545;
`;

const ROOM_TYPES = [
    'LIVING_ROOM',
    'KITCHEN',
    'BEDROOM',
    'BATHROOM',
    'GARAGE',
    'OFFICE'
];

const RoomFormModal = ({ room, onClose, onSubmit, title }) => {
    const [formData, setFormData] = useState(room || { name: '', roomType: '' });
    const [errors, setErrors] = useState({});
    const [serverError, setServerError] = useState('');

    const handleSubmit = async () => {
        try {
            setServerError('');
            setErrors({});
            await onSubmit(formData);
            onClose();
        } catch (error) {
            if (error.validationErrors) {
                setErrors(error.validationErrors);
            } else {
                setServerError('An error occurred while saving the room. Please try again.');
            }
        }
    };

    return (
        <ModalContent onClick={e => e.stopPropagation()}>
            <h2>{title}</h2>
            {serverError && <ServerError>{serverError}</ServerError>}
            <FormGroup>
                <Label htmlFor="name">Room Name</Label>
                <Input
                    id="name"
                    value={formData.name}
                    onChange={e => setFormData({ ...formData, name: e.target.value })}
                />
                {errors.name && <ErrorMessage>{errors.name}</ErrorMessage>}
            </FormGroup>
            <FormGroup>
                <Label htmlFor="roomType">Room Type</Label>
                <Select
                    id="roomType"
                    value={formData.roomType}
                    onChange={e => setFormData({ ...formData, roomType: e.target.value })}
                >
                    <option value="">Select a room type</option>
                    {ROOM_TYPES.map(type => (
                        <option key={type} value={type}>
                            {formatEnumValue(type)}
                        </option>
                    ))}
                </Select>
                {errors.roomType && <ErrorMessage>{errors.roomType}</ErrorMessage>}
            </FormGroup>
            <ModalActions>
                <button onClick={onClose}>Cancel</button>
                <button onClick={handleSubmit}>
                    {room ? 'Save Changes' : 'Create Room'}
                </button>
            </ModalActions>
        </ModalContent>
    );
};

export default RoomFormModal; 