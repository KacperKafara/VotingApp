import React, { forwardRef } from 'react';
import { Button, buttonVariants } from './ui/button';
import { Loader2 } from 'lucide-react';
import { VariantProps } from 'class-variance-authority';

interface Props
  extends React.ButtonHTMLAttributes<HTMLButtonElement>,
    VariantProps<typeof buttonVariants> {
  isLoading: boolean;
  disableButton?: boolean;
  text: string;
}

const LoadingButton = forwardRef<HTMLButtonElement, Props>(
  ({ text, disableButton, isLoading, ...props }, ref) => {
    return (
      <Button ref={ref} {...props} disabled={isLoading || disableButton}>
        {isLoading && <Loader2 className="mr-2 h-4 w-4 animate-spin" />}
        {text}
      </Button>
    );
  }
);

LoadingButton.displayName = 'LoadingButton';

export default LoadingButton;
