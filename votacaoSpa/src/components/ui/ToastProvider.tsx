import { createContext, useCallback, useContext, useMemo, useState } from 'react'
import type { ReactNode } from 'react'
import './ToastProvider.css'

const DEFAULT_TOAST_DURATION_MS = 4000

type ToastType = 'info' | 'warning' | 'error'

interface ToastInput {
  type?: ToastType
  title?: string
  message: string
  durationMs?: number
}

interface Toast {
  id: string
  type: ToastType
  title: string
  message: string
}

interface ToastContextValue {
  addToast: (toast: ToastInput) => void
  removeToast: (id: string) => void
}

const ToastContext = createContext<ToastContextValue | null>(null)

function randomId(): string {
  return `${Date.now()}-${Math.random().toString(16).slice(2)}`
}

interface ToastProviderProps {
  children: ReactNode
}

export function ToastProvider({ children }: ToastProviderProps) {
  const [toasts, setToasts] = useState<Toast[]>([])

  const removeToast = useCallback((id: string) => {
    setToasts((current) => current.filter((toast) => toast.id !== id))
  }, [])

  const addToast = useCallback(
    ({ type = 'info', title = 'Aviso', message, durationMs }: ToastInput) => {
      const toastDuration = durationMs ?? DEFAULT_TOAST_DURATION_MS
      const id = randomId()
      setToasts((current) => [...current, { id, type, title, message }])

      window.setTimeout(() => {
        removeToast(id)
      }, toastDuration)
    },
    [removeToast],
  )

  const contextValue = useMemo(
    () => ({ addToast, removeToast }),
    [addToast, removeToast],
  )

  return (
    <ToastContext.Provider value={contextValue}>
      {children}

      <section className="toast-stack" aria-live="polite" aria-label="Notificacoes">
        {toasts.map((toast) => (
          <article key={toast.id} className={`toast toast-${toast.type}`} role="alert">
            <div className="toast-content">
              <strong>{toast.title}</strong>
              <p>{toast.message}</p>
            </div>
            <button
              type="button"
              className="toast-close"
              onClick={() => removeToast(toast.id)}
              aria-label="Fechar notificacao"
            >
              x
            </button>
          </article>
        ))}
      </section>
    </ToastContext.Provider>
  )
}

export function useToast() {
  const context = useContext(ToastContext)

  if (!context) {
    throw new Error('useToast deve ser usado dentro de ToastProvider')
  }

  return context
}
