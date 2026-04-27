import React, { useState, useEffect } from 'react';
import { scopeAPI } from './services/api';

function SimpleScopeDemo() {
    const [requestCounter, setRequestCounter] = useState(0);
    const [sessionCounter, setSessionCounter] = useState(0);
    const [applicationCounter, setApplicationCounter] = useState(0);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        loadInitialValues();
    }, []);

    const loadInitialValues = async () => {
        try {
            const [requestRes, sessionRes, applicationRes] = await Promise.all([
                scopeAPI.getRequestValue(),
                scopeAPI.getSessionValue(),
                scopeAPI.getApplicationValue()
            ]);

            setRequestCounter(requestRes.data.counter);
            setSessionCounter(sessionRes.data.counter);
            setApplicationCounter(applicationRes.data.counter);
        } catch (error) {
            console.error(error);
        }
    };

    const handleRequestClick = async () => {
        setLoading(true);
        try {
            const res = await scopeAPI.incrementRequest();
            setRequestCounter(res.data.counter);
        } finally {
            setLoading(false);
        }
    };

    const handleSessionClick = async () => {
        setLoading(true);
        try {
            const res = await scopeAPI.incrementSession();
            setSessionCounter(res.data.counter);
        } finally {
            setLoading(false);
        }
    };

    const handleApplicationClick = async () => {
        setLoading(true);
        try {
            const res = await scopeAPI.incrementApplication();
            setApplicationCounter(res.data.counter);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="container mt-4">
            <h2>Scope Demonstration</h2>
            <table className="table table-bordered text-center">
                <thead>
                <tr>
                    <th>Scope</th>
                    <th>Counter</th>
                    <th>Action</th>
                </tr>
                </thead>

                <tbody>
                <tr>
                    <td>Request</td>
                    <td>{requestCounter}</td>
                    <td>
                        <button
                            className="btn btn-primary btn-sm"
                            onClick={handleRequestClick}
                            disabled={loading}
                        >
                            +
                        </button>
                    </td>
                </tr>

                <tr>
                    <td>Session</td>
                    <td>{sessionCounter}</td>
                    <td>
                        <button
                            className="btn btn-success btn-sm"
                            onClick={handleSessionClick}
                            disabled={loading}
                        >
                            +
                        </button>
                    </td>
                </tr>

                <tr>
                    <td>Application</td>
                    <td>{applicationCounter}</td>
                    <td>
                        <button
                            className="btn btn-warning btn-sm"
                            onClick={handleApplicationClick}
                            disabled={loading}
                        >
                            +
                        </button>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    );
}

export default SimpleScopeDemo;