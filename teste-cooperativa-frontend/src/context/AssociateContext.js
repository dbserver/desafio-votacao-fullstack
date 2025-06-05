import React, { createContext, useContext, useState } from 'react';

const AssociateContext = createContext();

export function AssociateProvider({ children }) {
  const [associate, setAssociate] = useState(null);

  return (
    <AssociateContext.Provider value={{ associate, setAssociate }}>
      {children}
    </AssociateContext.Provider>
  );
}

export function useAssociate() {
  return useContext(AssociateContext);
}