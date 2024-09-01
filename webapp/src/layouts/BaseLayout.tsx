import { FC } from "react";
import { Outlet } from "react-router-dom";


const BaseLayout: FC = () => {

  return (
    <div>
      <header>
        <h1>Base Layout</h1>
      </header>
      <main>
        <Outlet />
      </main>
    </div>
  );

};

export default BaseLayout;