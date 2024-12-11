import React, { createContext, useState, useEffect } from 'react';
import axios from 'axios';

export const AuthContext = createContext();

export const AuthProvider = ({ children }) => {
    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const token = localStorage.getItem('token');
        if (token) {
            axios.get('http://localhost:8080/api/auth/user', {
                headers: { Authorization: `Bearer ${token}` }
            }).then(response => {
                setUser(response.data);
                setLoading(false);
            }).catch(() => {
                setUser(null);
                setLoading(false);
            });
        } else {
            setLoading(false);
        }
    }, []);

    const login = (token, userData) => {
        localStorage.setItem('token', token);
        setUser(userData);
    };

    const logout = () => {
        localStorage.removeItem('token');
        setUser(null);
        window.dispatchEvent(new CustomEvent('userDataUpdated', { detail: { balance: 0 } }));
    };

    return (
        <AuthContext.Provider value={{ user, loading, login, logout, setUser }}>
            {children}
        </AuthContext.Provider>
    );
};

export default AuthProvider;
