/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { MaTestModule } from '../../../test.module';
import { InteractionTypeUpdateComponent } from 'app/entities/interaction-type/interaction-type-update.component';
import { InteractionTypeService } from 'app/entities/interaction-type/interaction-type.service';
import { InteractionType } from 'app/shared/model/interaction-type.model';

describe('Component Tests', () => {
    describe('InteractionType Management Update Component', () => {
        let comp: InteractionTypeUpdateComponent;
        let fixture: ComponentFixture<InteractionTypeUpdateComponent>;
        let service: InteractionTypeService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [MaTestModule],
                declarations: [InteractionTypeUpdateComponent]
            })
                .overrideTemplate(InteractionTypeUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(InteractionTypeUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(InteractionTypeService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new InteractionType(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.interactionType = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.update).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );

            it(
                'Should call create service on save for new entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new InteractionType();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.interactionType = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.create).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );
        });
    });
});
