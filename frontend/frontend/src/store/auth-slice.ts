import { createSlice, type PayloadAction } from '@reduxjs/toolkit';


type CartState = {
  isAuthenticated: boolean;
};

const initialState: CartState = {
  isAuthenticated: false,
};

export const authSlice = createSlice({
  name: 'auth',
  initialState,
  reducers: {
    authentication(
      state,
      action: PayloadAction<{ id: string; title: string; price: number }>
    ) {
    //   const itemIndex = state.items.findIndex(
    //     (item) => item.id === action.payload.id
    //   );

    //   if (itemIndex >= 0) {
    //     state.items[itemIndex].quantity++;
    //   } else {
    //     state.items.push({ ...action.payload, quantity: 1 });
    //   }
    },
  },
});

export const { authentication } = authSlice.actions;
